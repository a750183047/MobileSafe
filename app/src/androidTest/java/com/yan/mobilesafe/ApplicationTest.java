package com.yan.mobilesafe;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.yan.mobilesafe.DataBase.BlackNumberDb;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Context context;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.context = getContext();
    }
    public void testCreateDB(){
        BlackNumberDb blackNumberDb = new BlackNumberDb(context);

    }
    public void testAdd(){
        BlackNumberDb blackNumberDb = new BlackNumberDb(context);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Long number = 13300000000l +i;
            blackNumberDb.addNumber(number +"",String.valueOf(random.nextInt(3) + 1));
        }
    }
    public void testDelete(){
        BlackNumberDb blackNumberDb = new BlackNumberDb(context);
        boolean b = blackNumberDb.deleteNumber("13300000000");
        assertEquals(true,b);
    }
    public void testChangeMode(){
        BlackNumberDb blackNumberDb = new BlackNumberDb(context);
        boolean b = blackNumberDb.changeNumberMode("13300000005", "2");
        assertEquals(true,b);
    }
    public void testFindMode(){
        BlackNumberDb blackNumberDb = new BlackNumberDb(context);
        String number = blackNumberDb.findNumber("13300000005");
        assertEquals("2",number);
    }
}