package tct;



import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.io.ByteArrayInputStream;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.io.File;





import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;



public class GetPhotofromApplet extends HttpServlet {

    //  Servlet server-side code to read a serialized

    //  student object from an applet.



    public void doPost(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {



        System.out.println("Servlet begin...");



        try {

            // read a String-object from applet

            // instead of a String-object, you can transmit any object, which

            // is known to the servlet and to the applet



            //Read image data from connection

            System.out.println("Prepare to read image data");

            InputStream in = request.getInputStream();

            ByteArrayOutputStream bos = new ByteArrayOutputStream(20480);

            byte[] buf = new byte[1024];

            int rtn = in.read(buf);

            while (rtn >= 0) {

                bos.write(buf, 0, rtn);

                rtn = in.read(buf);

            }

            in.close();

            byte[] data = bos.toByteArray();

            System.out.println("Read image data:" + data.length + " bytes");

            data=ImageUploadTool.getImageData(data);
            System.out.println("ImageUploadTool.getImageData -> data:" + data.length + " bytes");
            
            //save data to the database and set the flag to true or false

            //depending on if it is successful

            String id = request.getParameter("id");

            System.out.println("Student id: " + id);

            System.out.println("Saving data to database");

            boolean flag = saveImage(id, data);



			//Get Moodle ID so that the picture in the moodle picture folder

			System.out.println("studentID=:"+id);

            MoodleConnector mConnector=new MoodleConnector(); 

			String moodleID=mConnector.getUserID(id);

			System.out.println("moodleID=:"+moodleID);

            String path="D:/_Server_/xampp/htdocs/moodle_hs26/user/0/"+moodleID;

			boolean flag1 = saveImagetoFile(path,id,data);

            //set moodle picture flag 

			mConnector.setPictureFlag(id);



            //Send response back to client side

            String result = (flag) ? "OK" : "FATAL";

            System.out.println("Result: " + result);

            byte[] resp = result.getBytes("utf-8");

            response.setContentType("application/x-java-serialized-object");

            response.setContentLength(resp.length);

            response.getOutputStream().write(resp);



        } catch (Exception e) {

            e.printStackTrace();

        }



        System.out.println("Servlet end.");

    }



    public boolean saveImage(String id, byte[] b) {



        boolean flag = false;



        try {





            //connect to database

            ConnectionParameter.loadParameter();

            String driver = ConnectionParameter.JDBC_DRIVER;

            String url = ConnectionParameter.CONN;

            String user =ConnectionParameter.USER_NAME;

            String pwd = ConnectionParameter.PW;

            String imageTable = ConnectionParameter.imageTable;



            String selectSql = "select * from " + imageTable + " where id=?";

            String updateSql = "update " +imageTable+" set image=? where id=?";

            String sql = "insert into " + imageTable + "(id,image,creation_day) values(?,?,CURDATE())";

            Connection conn = null;

            PreparedStatement psmtSelect = null;

            PreparedStatement psmtDel = null;

            PreparedStatement psmtInsert = null;

            ResultSet rs = null;

            try {

                Class.forName(driver);

                conn = DriverManager.getConnection(url, user, pwd);



                if (!conn.isClosed())

                    System.out

                            .println("Successfully connected to MySQL server");

            } catch (Exception e) {

                System.out.println("Connect server ERROR:" + e.getMessage());

            }



            try {

                //select

                psmtSelect = conn.prepareStatement(selectSql);

                psmtSelect.setString(1, id);

                rs = psmtSelect.executeQuery();

                if(rs.next()) {

                   //update record

                    psmtDel = conn.prepareStatement(updateSql);

                    psmtDel.setBytes(1, b);

					psmtDel.setString(2, id);

                    psmtDel.execute();



				System.out.println("You update picture successfully!");

               }



				else {//insert new record

                psmtInsert = conn.prepareStatement(sql);

                System.out.println(b.length);



                psmtInsert.setString(1, id);

                psmtInsert.setBytes(2, b);





                psmtInsert.execute();



                System.out.println("You insert picture successfully!");



			   }

                conn.close();



            } catch (Exception e) {

                e.printStackTrace();

            }



            //set the flag to true after successfully save the data

            flag = true;

        } catch (Exception e) {

            e.printStackTrace();

        }



        return flag;

    }





	public boolean saveImagetoFile(String path,String id, byte[] b){



		boolean flag = false;

		try{

		ByteArrayInputStream bais = new ByteArrayInputStream(b);

        BufferedImage bi = ImageIO.read((InputStream) bais);

        File f1=new File(path);

		if(!f1.exists()) 

        f1.mkdirs();



        ImageIO.write(bi, "jpg", new File(path,"f1.jpg"));

		ImageIO.write(bi, "jpg", new File(path,"f2.jpg"));

         flag = true;

		 } catch (Exception e) {

            e.printStackTrace();

        }



        return flag;

    }





}
