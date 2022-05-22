package com.assignment.visma_test_project.meeting;
import com.assignment.visma_test_project.enums.Category;
import com.assignment.visma_test_project.enums.Type;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Meeting {

    @SerializedName("id")
    private Long id;

    @SerializedName("attendees")
    private List<String> attendees;

    @SerializedName("name")
    private String name;

    @SerializedName("responsiblePerson")
    private String responsiblePerson;

    @SerializedName("description")
    private String description;

    @SerializedName("category")
    private Category category;

    @SerializedName("type")
    private Type type;

    @SerializedName("startDate")
    private Date startDate;

    @SerializedName("endDate")
    private Date endDate;

}
