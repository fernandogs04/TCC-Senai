<?php
    mysqli_report(MYSQLI_REPORT_ALL ^ MYSQLI_REPORT_INDEX);
    function conectadb () {
        $endereco = "localhost";
        $usuario = "root";
        $senha = "";
        $banco = "sistemaemprestimos";

        try {
            $con = new mysqli ($endereco, $usuario, $senha, $banco);
            return $con;
        }
        catch (Exception $e) {
            echo $e->getMessage();
        }
    }
?>