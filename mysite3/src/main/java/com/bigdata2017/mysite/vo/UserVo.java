package com.bigdata2017.mysite.vo;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UserVo {
	private Long no;
	
	/* name이 비어있으면 에러(UserVo객체를 받을 때, @Valid가 붙어있는 경우 아래 내용들을 체크 */
	@NotEmpty
	@Length(min=2, max=5)
	private String name;
	
	@NotEmpty
	@Pattern(regexp="^[0-9a-zA-Z]{4,8}$")
	private String password;
	/* Hibernate에서 @Email 제공해줌, 혹은 직접 패턴을 정의해 줘도 됨, 혹은 커스텀 Valid 어노테이션 만드는 법을 찾아서 주민번호, 핸드폰번호 등을 어노테이션으로 만들어 쓸 수도 있음 */
	@Email
//	@Pattern(regexp="^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$")
	private String email;
	
	/* gender는 form에서 들어오면 하나값을 가지고 와서 굳이 안해줘도 무방 */
//	@Pattern(regexp="^(FEMALE|MALE)$")
	private String gender;
	
	private String joinDate;
	private String role;
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "UserVo [no=" + no + ", name=" + name + ", password=" + password + ", email=" + email + ", gender="
				+ gender + ", joinDate=" + joinDate + "]";
	}
}
