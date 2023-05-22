package factory.first.may.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import java.beans.Statement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

@SpringBootApplication
class BackendApplication {

    static void main(String[] args) {
        SpringApplication.run(BackendApplication, args)
        println 'Server started'

        String dbURL = "jdbc:sqlserver://srv-intermech;databaseName=proiz;integratedSecurity=false;encrypt=true;trustServerCertificate=true;";
        String username = "search";
        String password = "srh";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Connection conn;

        try {

            conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Connected");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        //MSSQL Server
        String sql = "USE proiz SELECT tab_n,fam,nam ,snm ,rab.kod_prof ,dt_rozd AS date_birthday ,dts AS date_start ,dtu AS date_dismissals, re.kod_prof, re.name_kat, pdr as pdr_id, pdr.name_pdr as pdr_name, pdr.shortname as pdr_shortname FROM [proiz].[dbo].[spr_rab] rab inner join (SELECT [kod_prof]       ,prof.[kod_kat]       ,[tarif_oklad],   kat.name_kat   FROM [proiz].[dbo].[eko_kat] prof   join eko_spr_kateg kat on kat.kod_kat=prof.kod_kat) re on rab.kod_prof = re.kod_prof inner join (select kod_pdr,name_pdr,shortname from proiz.dbo.eko_st_spr_pdr) pdr on cast(pdr.kod_pdr as Integer) = rab.pdr";

        if (conn != null) {
            java.sql.Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);

            int count = 0;

            while (result.next()) {
                String name = result.getString(2);
                String pass = result.getString(3);
                //String fullname = result.getString("fullname");
                //String email = result.getString("email");

                String output = "User #%d: %s - %s ";
                //println(new String(String.format(output, ++count, name, pass).getBytes('windows-1251'),'UTF-8'));
                println(String.format(output, ++count, name, pass));
            }
        }

    }
}