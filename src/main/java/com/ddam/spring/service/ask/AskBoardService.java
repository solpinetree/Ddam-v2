package com.ddam.spring.service.ask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddam.spring.domain.ask.AskBoard;
import com.ddam.spring.domain.ask.AskFile;
import com.ddam.spring.domain.user.User;
import com.ddam.spring.repository.ask.AskBoardRepository;
import com.ddam.spring.repository.ask.AskFileRepository;
import com.ddam.spring.repository.user.UserRepository;

@Service
public class AskBoardService {

	@Autowired
	private AskBoardRepository repository;
	
	@Autowired
	private AskFileRepository filerepository;

	@Autowired
	private UserRepository userRepository;

	public AskBoardService() {
		System.out.println("Ask_boardService() 생성");
	}
	
	// 문의사항 게시판 목록페이지
	public List<AskBoard> list() {
		return repository.findAll(Sort.by(Direction.ASC, "abid"));
	}

	// 문의사항 작성페이지(제목, 내용)
	@Transactional
	public Long saveAsk_board(AskBoard dto) {
		
		User user = null;
		// 현재 로그인한
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null) {
			user = (User)authentication.getPrincipal();
		}
		
		user = userRepository.findByUsername(user.getUsername()).orElseThrow();
		
		dto.setUid(user);
		
		repository.save(dto);
		return dto.getAbid();
	}

	// 문의사항 뷰페이지(제목, 내용)
	// 특정 abid 의 글 읽어오기 + 조회수 증가
	@Transactional   // 해당 메소드는 하나의 트랜잭션으로 처리함.
	public List<AskBoard> viewByAbid(long abid){
		
		List<AskBoard> list = new ArrayList<>();
		
		AskBoard data = repository.findById(abid).orElse(null); // SELECT
		if(data != null) {
			data.setView_cnt(data.getView_cnt() + 1);
			repository.saveAndFlush(data);  // UPDATE
			list.add(data);
		}
		return list;  // 읽어오기
	}
	
	//문의사항 읽어오기(제목, 내용)
	public List<AskBoard> selectByAbid(long abid){
		List<AskBoard> list = new ArrayList<>();
		list.add(repository.findById(abid).orElse(null));
		return list;
	}
	
	//문의사항 수정(제목, 내용)
	public int update(AskBoard dto, String originalFileName) {
		int cnt = 0;
		AskBoard data = repository.findById(dto.getAbid()).orElse(null);
		List<AskFile> files = filerepository.findByBoard(data);
		AskFile file = files.get(0);
		
		if(data != null) {
				
			data.setContent(dto.getContent());
			data.setSubject(dto.getSubject());
			file.setOriginalname(originalFileName);
			file.setFilename(originalFileName);
			
			repository.saveAndFlush(data);  // UPDATE
			filerepository.saveAndFlush(file);
			cnt = 1;
		}
		return cnt;
	}
	
	//문의사항 삭제
	public int deleteByUid(long abid) {
		AskBoard board = repository.findById(abid).orElse(null);
		List<AskFile> files = filerepository.findByBoard(board);
		files.forEach(i -> filerepository.delete(i));
		repository.deleteById(abid);
		repository.flush();
		return 1;
	}
	
	//문의사항 답변페이지(제목,내용)
	@Transactional
	public List<AskBoard> selectByAbid_Admin(long abid){
		List<AskBoard> list = new ArrayList<>();
		list.add(repository.findById(abid).orElse(null));
		return list;
	}	

	// 문의사항 답변페이지(답변작성)
	@Transactional
	public int update_admin(AskBoard dto) {
		int cnt = 0;
		AskBoard data = repository.findById(dto.getAbid()).orElse(null);
		
		if(data != null) {
			data.setAnswer(dto.getAnswer());
			repository.saveAndFlush(data);  // UPDATE
			cnt = 1;
		}
		return cnt;
	}
	


}
