package com.codeterian.performance.domain.performance;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class PerformanceImage {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String imageUrl;

	public PerformanceImage(String url) {
		this.imageUrl = url;
	}

}
