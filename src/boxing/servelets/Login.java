package boxing.servelets;


import com.alibaba.fastjson.JSON;

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

import boxing.beans.BaseBean;
import boxing.beans.LoginBean;
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
        BaseBean baseBean = new BaseBean();

        String name = request.getParameter("name");
        String psd = request.getParameter("psd");
        log.info("name=" + name + "  psd=" + psd);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(psd)) {
            LoginBean loginBean = DBHelper.login(name, psd);
            baseBean.setObject(loginBean);
            if (loginBean.getUserId() == 0){
                baseBean.setCode(203);
                baseBean.setMessage("用户不存在");
            }else {
                baseBean.setCode(200);
                baseBean.setMessage("登录成功");
            }
        } else {
            baseBean.setCode(201);
            baseBean.setMessage("参数不正确");
            baseBean.setObject(new Object());
        }
        out.print(JSON.toJSONString(baseBean));
    }
}
