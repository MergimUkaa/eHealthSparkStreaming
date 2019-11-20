package Models;

import java.io.Serializable;
import java.util.UUID;

public class testModel implements Serializable {
    private UUID id;
    private Integer age;
    private String name;

    testModel() {}
    public testModel(UUID id, Integer age, String name){
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
