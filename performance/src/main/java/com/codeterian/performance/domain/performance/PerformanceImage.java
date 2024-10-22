package com.codeterian.performance.domain.performance;

import java.io.Serializable;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PerformanceImage implements Serializable {

	private String titleImage;
	private List<String> images;

	public static PerformanceImage addPerformanceImage(String titleImage, List<String> images){
		return new PerformanceImage(titleImage, images);
	}

	public static PerformanceImage modifyPerformanceImage(String titleImage, List<String> images){
		return new PerformanceImage(titleImage, images);
	}
}
