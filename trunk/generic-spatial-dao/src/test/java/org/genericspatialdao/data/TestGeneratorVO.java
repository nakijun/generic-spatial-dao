package org.genericspatialdao.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "my_generator", sequenceName = "my_seq", initialValue = 100, allocationSize = 1)
public class TestGeneratorVO {

	@Id
	@GeneratedValue(generator = "my_generator", strategy = GenerationType.SEQUENCE)
	/*
	 * @GenericGenerator(name = "my_generator", strategy =
	 * "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
	 * 
	 * @Parameter(name = "sequence_name", value = "my_seq"),
	 * 
	 * @Parameter(name = "initial_value", value = "100") })
	 */
	private long id;

	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TestGeneratorVO [id=" + id + ", name=" + name + "]";
	}
}
