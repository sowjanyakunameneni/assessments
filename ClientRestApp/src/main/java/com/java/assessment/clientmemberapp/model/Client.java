package com.java.assessment.clientmemberapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DynamicUpdate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The type Client model.
 *
 * @author Sowjanya Gonuguntla
 */
@Entity
@Table(name = "CLIENT", uniqueConstraints = @UniqueConstraint(columnNames = { "MOBILE_NUMBER", "ID_NUMBER" }))
@DynamicUpdate(value = true)
@ApiModel(description = "Class representing a Client with all fields")
public class Client {

	public Client(Long id, String firstName, String lastName, String mobileNumber, String idNumber, String address) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.idNumber = idNumber;
		this.address = address;
	}

	public Client() {
		super();
	}

	public Client(Long id, @NotNull(message = "Valid ID Number is mandatory") String idNumber, String address) {
		super();
		this.id = id;
		this.idNumber = idNumber;
		this.address = address;
	}

	public Client(String firstName, String lastName, String mobileNumber, String idNumber, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.idNumber = idNumber;
		this.address = address;
	}

	public Client(
			@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 digits") String mobileNumber,
			String address) {
		super();
		this.mobileNumber = mobileNumber;
		this.address = address;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@Column(name = "ID")
	@ApiModelProperty(example = "1", notes = "ID is auto generated Primary Key")
	private Long id;

	@Column(name = "FIRST_NAME")
	@NotNull(message = "First Name is mandatory")
	@ApiModelProperty(example = "Sowjanya", notes = "First Name of the CLient and its mandatory to create client record")
	private String firstName;

	@Column(name = "LAST_NAME")
	@NotNull(message = "Last Name is mandatory")
	@ApiModelProperty(example = "Gonuguntla", notes = "Last Name of the CLient and its mandatory to create client record")
	private String lastName;

	@Column(name = "MOBILE_NUMBER")
	@Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number should be 10 digits")
	@ApiModelProperty(example = "9898989898", notes = "Mobile number should be 10 digits and No Duplicates allowed")
	private String mobileNumber;

	@Column(name = "ID_NUMBER")
	@NotNull(message = "Valid ID Number is mandatory")
	@ApiModelProperty(example = "5512285151085", notes = "ID number should be 13 digits and No Duplicates allowed , its should be in "
			+ "yymmdd[0000 - 9999][0 or 1][0-9][0-9]")
	private String idNumber;

	@Column(name = "PHYSICAL_ADDRESS")
	@ApiModelProperty(example = "JP Nagar, Bangalore", notes = "Address of the Client")
	private String address;

	public Long getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNumber="
				+ mobileNumber + ", idNumber=" + idNumber + ", address=" + address + "]";
	}

}
