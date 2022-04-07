<?php
    session_start();

    if (isset($_SESSION["logado"]) && $_SESSION["logado"] == true) {
        header("Location: menuPrincipal.php");
    }
    else {
        header("Location: login.php");
    }
?>