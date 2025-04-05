<#ftl encoding="UTF-8">
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Токен подтверждения регистрации ${email}</title>
    <style>
        #body {
            display: flex;
            flex-direction: column;
            font-family: "Segoe UI", serif;
            align-items: center;
            background-color: #607FF2;
            font-size: 20px;
            height: 100%;
        }

        #div {
            display: block;
            flex-direction: column;
            margin: 10%;
            font-family: "Segoe UI", serif;
            background-color: white;
            padding: 3%;
            border-radius: 20px;
            color: black;
        }

        #a {
            text-decoration: none;
        }
    </style>
</head>
<body>
<div id="body">
    <div id="div">


        <h1 style="text-align: center">Registration</h1>
        <p>Здравствуйте ${name}!</p>
        <p>Внесите токен в форму подтверждения на сайте</p>
        <p>Токен регистрации ${token}!</p>
        <p>Перейдите по адресу подтверждения ${confirm_url}</p>
    </div>
</div>
</body>
</html>