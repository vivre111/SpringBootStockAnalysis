package com.austin.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Repository("jDBCController")
public class JDBCController {
    @Autowired
    JdbcTemplate jdbc;
    String dbName = "test";
    String tableName = "StockDataTest";
    String fileDir = "/home/phantomoflamancha/java-projects/US/daily-normal";

    public Stock findById(int id){
        try{
            return jdbc.queryForObject(String.format("select * from %s where id=?", tableName), BeanPropertyRowMapper.newInstance(Stock.class), id);
        }catch(IncorrectResultSizeDataAccessException ex){
            return null;
        }
    }

    public List<Stock> findByName(String name){
        try{
            return jdbc.query(String.format("select * from %s where name=?", tableName),new Object[]{name}, BeanPropertyRowMapper.newInstance(Stock.class));
        }catch(IncorrectResultSizeDataAccessException ex){
            return null;
        }
    }
    public void createTableIfNotExist(){
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        int count = jdbc.queryForObject(sql, Integer.class, dbName, tableName);
        boolean tableExists = count > 0;

        if(!tableExists){
            jdbc.execute(String.format( "CREATE TABLE %s (id int(11) not null auto_increment,date date, open DECIMAL(10 , 2 ), " +
                    "high DECIMAL(10 , 2 ),low DECIMAL(10 , 2 ),close DECIMAL(10 , 2 ),trade DECIMAL(10 , 2 )," +
                    " amount decimal(20,2),name varchar(10), primary key (`id`), index(id, date, name));", tableName));
        }
    }
    public void insertAllCSV(int batchNumber) throws FileNotFoundException, ParseException{
        File dirPath = new File(fileDir);
        String[] contents = dirPath.list();
        for(String file : contents){
            batchInsertFromFile(file, batchNumber);
        }
    }

    public void batchInsertHelper(List<Stock> stocks, int batchNumber, String filename){
        String sql = String.format( "insert into %s(date,open,high,low,close,trade,amount,name) values (?,?,?,?,?,?,?,?)", tableName);
        if(stocks.size()==batchNumber){
            jdbc.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setDate(1, new java.sql.Date(stocks.get(i).getDate().getTime()));
                    ps.setFloat(2, stocks.get(i).getOpen());
                    ps.setFloat(3, stocks.get(i).getHigh());
                    ps.setFloat(4, stocks.get(i).getLow());
                    ps.setFloat(5, stocks.get(i).getClose());
                    ps.setFloat(6, stocks.get(i).getTrade());
                    ps.setFloat(7, stocks.get(i).getAmount());
                    ps.setString(8, filename);
                }

                @Override
                public int getBatchSize() {
                    return batchNumber;
                }
            });
            stocks.clear();
        }
    }

    public void batchInsertFromFile(String filename, int batchNumber) throws FileNotFoundException, ParseException {
        Scanner sc;
        sc = new Scanner(new File(fileDir + "/" + filename));
        sc.useDelimiter("\r\n");
        boolean firstline = true;
        List<Stock> stocks = new ArrayList<Stock>();
        try {
            while (sc.hasNext()) {
                String st = sc.next();
                //skip first line
                if (firstline) {
                    firstline = false;
                    continue;
                }
                String[] row = st.split(",");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date;
                date = dateFormat.parse(row[0]);
                Stock stock = new Stock(date, Float.parseFloat(row[1]), Float.parseFloat(row[2]),
                        Float.parseFloat(row[3]), Float.parseFloat(row[4]), Float.parseFloat(row[5]), Float.parseFloat(row[6]), filename.split("\\.")[0]);

                stocks.add(stock);

                if (stocks.size() == batchNumber) {
                    batchInsertHelper(stocks, batchNumber, filename);
                }
            }
            batchInsertHelper(stocks, batchNumber, filename);
            System.out.println(String.format("Data from %s inserted Successfully", filename));
        }finally {
            sc.close();
        }
    }
}

