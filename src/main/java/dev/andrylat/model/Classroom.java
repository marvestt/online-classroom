package dev.andrylat.model;

public class Classroom {

	private long classId;
	private long mainTeacherId;
	private String name;
	private String description;


	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getMainTeacherId() {
		return mainTeacherId;
	}

	public void setMainTeacherId(long mainTeacherId) {
		this.mainTeacherId = mainTeacherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Class[class_id=%d, main_teacher_id=%d, name=%d,description='%s']", 
				classId, mainTeacherId, name,description);
	}
	
	
}
