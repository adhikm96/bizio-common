package com.thebizio.commonmodule.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class LastUpdateDetail {

	@CreatedDate
	@Column(name = "created_at", updatable = false, nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
	private LocalDateTime created;

	@LastModifiedDate
	@Column(name = "modified_at")
	private LocalDateTime modified;

	@Column(name = "created_by", updatable = false)
	@CreatedBy
	private String createdBy;

	@Column(name = "modified_by")
	@LastModifiedBy
	private String modifiedBy;
}