package com.devglan.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DsController {
	
	@RequestMapping("/command")
	public String createCommand(@RequestParam String id) throws InterruptedException {
		System.out.println(Thread.currentThread().getName());
        try
        {
        	Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/asimily","postgres","postgres");
            Statement stmt = conn.createStatement();
            stmt.execute("NOTIFY a" + id);
            stmt.close();
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
		return "Command created";
	}
    
}
