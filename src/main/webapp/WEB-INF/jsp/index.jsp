<%--
  Created by IntelliJ IDEA.
  User: Khadija
  Date: 1/22/17
  Time: 4:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>HelloWorldSpringMVC</title>
  </head>
  <script type="text/javascript">

    function callTheFileUploadClient() {
        $.post("../rest/upload/image", {}, function() {
        });
    }

  </script>
  <body>

    <div>
      <p>Let's Fire a Image Upload</p>
      <hr />
      <input type="button" value="Click To Upload a File" onclick="">
    </div>

  </body>
</html>
