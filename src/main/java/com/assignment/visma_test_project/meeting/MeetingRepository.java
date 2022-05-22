package com.assignment.visma_test_project.meeting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class MeetingRepository {
    Gson gson;

    @Autowired
    public MeetingRepository(Gson gson) {
        this.gson = gson;
    }

    public void save(List<Meeting> meetings) throws IOException {
        Writer writer = new FileWriter("src/main/resources/meetings.json");
        gson.toJson(meetings, writer);
        writer.flush();
        writer.close();
    }

     List<Meeting> retrieve() throws IOException {
        Reader reader = new FileReader("src/main/resources/meetings.json");
        List<Meeting> meetings = gson.fromJson(reader, new TypeToken<List<Meeting>>() {}.getType());

        if (Objects.isNull(meetings)) meetings = new ArrayList<Meeting>();
        reader.close();

        return meetings;
    }
}
