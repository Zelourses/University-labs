<?php
session_start();
?>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF">
    <title>Результат вычислений</title>
    <link href="../css/style2.css" rel="stylesheet">
    <link rel="icon" type="image/png" href="../images/favicon.png" />
</head>
<body>
<table cellspacing="0" id="maket">
    <tr>
        <td id="header" colspan="2" >
            <div><label>БАРСУКОВ ИЛЬЯ P3201 ВАРИАНТ 201000</label></div>
        </td>

            <?php if (count($_SESSION['result']) > 0): ?>
                <table class="table">
                    <tr>
                        <th>
                            <label><?php echo implode('</th></label><th><label>', array_keys(current($_SESSION['result']))); ?>
                        </th>
                    </tr>
                    <?php foreach ($_SESSION['result'] as $row): array_map('htmlentities', $row); ?>
                        <tr>
                            <td><label><?php echo implode('</td></label><td><label>', $row); ?></td>
                        </tr>
                    <?php endforeach; ?>
                </table>
            <?php endif; ?>
    </tr>
    <a href="../index.html" class="text">Попытаться снова</a>

</table>
</body>
</html>
