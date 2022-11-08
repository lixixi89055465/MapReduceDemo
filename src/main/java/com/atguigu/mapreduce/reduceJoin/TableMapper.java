package com.atguigu.mapreduce.reduceJoin;

import com.fasterxml.jackson.module.jaxb.ser.DomElementJsonSerializer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class TableMapper extends Mapper<LongWritable,Text, Text,TableBean> {

    private String fileName;
    private Text outK=new Text();
    private TableBean outV=new TableBean();


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit split= (FileSplit) context.getInputSplit();
        fileName = split.getPath().getName();

    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();
        //2 判断是哪个文件的
        if(fileName.contains("order")){
            String[] split = line.split("\t");
            // 封装 K V
            outK.set(split[1]);
            outV.setId(split[0]);
            outV.setPid(split[1]);
            outV.setAmount(Integer.parseInt(split[0]));
            outV.setPname("");
            outV.setFlag("order");
        }else{
            String[] split = line.split("\t");
            outK.set(split[0] );
            outV.setId("");
            outV.setPid(split[0]);
            outV.setAmount(0);
            outV.setPname(split[1]);
            outV.setFlag("pd");
        }
        // 写出
        context.write(outK,outV);
    }
}
