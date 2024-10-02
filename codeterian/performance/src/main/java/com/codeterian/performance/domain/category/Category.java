package com.codeterian.performance.domain.category;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.codeterian.performance.domain.performance.Performance;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category{

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

	private boolean isDeleted = false;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Category> children = new HashSet<>();

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Performance> performances = new HashSet<>();

	public void modifyCategoryName(String name) {
		this.name = name;
	}

	public void modifyParentId(UUID parentId) {
		this.parent = Category.builder().id(parentId).build();
	}

	// public void softDelete(Integer handlerId) {
	// 	delete(handlerId);
	// }

}