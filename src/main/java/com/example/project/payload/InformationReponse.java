package com.example.project.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformationReponse {
    public String name;
    public String birthday;
    public String profession;
    public String country;
    public Long id_infomation_user;
    public String name_file;
    public Long id_user;
    public String username;
}
