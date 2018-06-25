package boxing.servelets;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import boxing.dao.DBHelper;
import boxing.utils.TextUtils;

/**
 * @author Liuyuli
 * @date 2018/6/22.
 */
@WebServlet(name = "Login")
public class Login extends HttpServlet {
    private static Logger log;

    static {
        BasicConfigurator.configure();
        log = Logger.getLogger(Login.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        //设置网页响应类型
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String psd = request.getParameter("psd");
        log.info("name=" + name + "  psd=" + psd);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(psd)) {
            boolean login = DBHelper.login(name, psd);
            //实现具体操作
            out.println("登录" + login);
        } else {
            out.println("参数不正确");
        }
    }
}
