package bva.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "F3", urlPatterns = "/ccc")
public class F3 implements Filter {
    public void destroy() {
        System.out.println("F3: destroy");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (((HttpServletRequest) req).getMethod().equals("POST") &&
                req.getParameter("value-2").equals(req.getParameter("value-3"))  ) {
            System.out.println("F3: Error");
            resp.getWriter()
                    .println("F3: Error: (value-2 == value-3)");
        }
        else {
            System.out.println("F3: doFilter");
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("F3: init");
    }

}
