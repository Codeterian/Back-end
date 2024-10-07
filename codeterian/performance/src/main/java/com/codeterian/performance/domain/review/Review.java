package com.codeterian.performance.domain.review;

import java.util.UUID;

import com.codeterian.performance.domain.performance.Performance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "performance_id")
	private Performance performance;

	private String title;
	private String description;
	private int rating;

	// 유저
	// private Long userId;

	// 나중에 baseEntity 사용하면 삭제할 예정
	private boolean isDeleted = false;

	public void update(String title, String description, Integer rating, Performance performance) {
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.performance = performance;
	}
}
