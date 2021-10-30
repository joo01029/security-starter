package com.example.securitytest.domain.entity;

import com.example.securitytest.enums.Role;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {@Index(columnList = "id")})
public class User {
	@Id
	@GeneratedValue
	private Long idx;

	@Column(unique = true, length = 50)
	private String id;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Role role;
}
