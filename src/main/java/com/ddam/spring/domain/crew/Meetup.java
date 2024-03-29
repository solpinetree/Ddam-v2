package com.ddam.spring.domain.crew;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Meetup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	
	@ManyToOne
	@JoinColumn(name="crewId")
	@JsonIgnoreProperties({"crewAdmin, meetupLists"})
	private Crew crew;
	
	private String location;
	
	private String fee;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
//	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime datetime;
	
	@OneToMany(mappedBy = "meetup", cascade = CascadeType.REMOVE)
	private Set<MeetupUser> participantList = new HashSet<>();
	
	private Long memberLimit;
	
	@Column(columnDefinition = "LONGTEXT")
	private String description;
	
	@CreationTimestamp
	private Timestamp createDate;
	@CreationTimestamp
	private Timestamp updateDate;

}
