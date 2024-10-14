package com.codeterian.performance.domain.category;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.codeterian.common.infrastructure.entity.BaseEntity;
import com.codeterian.performance.domain.performance.Performance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Category extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parent;

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

	public void deleteCategory(Long handlerId) {
		delete(handlerId);
	}

}