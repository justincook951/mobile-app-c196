package com.example.c196project.database.mentor;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "mentor")
public class MentorEntity
{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String phone;
    private String email;

    @Ignore
    public MentorEntity() {
    }

    public MentorEntity(int id, String name, String phone, String email)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public MentorEntity(String name, String phone, String email)
    {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
