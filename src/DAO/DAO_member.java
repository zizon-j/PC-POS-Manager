package DAO;

import DTO.DTO_member;
import Jdbc.Pc_Pos_Db_Connection;

import java.sql.PreparedStatement;

public class DAO_member {
    PreparedStatement preparedStatement = null;

    public void InsertMember(DTO_member dto_member){
        Pc_Pos_Db_Connection pcPosDbConnection = new Pc_Pos_Db_Connection();

    }

}
