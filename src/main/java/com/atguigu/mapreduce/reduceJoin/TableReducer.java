package com.atguigu.mapreduce.reduceJoin;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.codehaus.jackson.map.util.BeanUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class TableReducer extends Reducer<Text, TableBean, TableBean, NullWritable> {
    @Override
    protected void reduce(Text key, Iterable<TableBean> values, Context context) throws IOException, InterruptedException {
        //准备初始化集合
        ArrayList<TableBean> orderBeans = new ArrayList<TableBean>();
        TableBean pdBean = new TableBean();
        for (TableBean value : values) {
            if ("order".equals(value.getFlag())) {
                TableBean tmpTableBean = new TableBean();
                try {
                    BeanUtils.copyProperties(tmpTableBean, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                orderBeans.add(tmpTableBean);
            } else {
                try {
                    BeanUtils.copyProperties(pdBean, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        for (TableBean orderBean : orderBeans) {
            orderBean.setPname(pdBean.getPname());
            context.write(orderBean, NullWritable.get());
        }

    }
}
