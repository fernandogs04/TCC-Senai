<?php
    $nome = $cpf = $email = $senha = $confirmaSenha = "";
    $erroNome = $erroCpf = $erroEmail = $erroSenha = $erroConfirmaSenha = $mensagemRegistro = "";

    session_start();

    if (isset($_SESSION["logado"]) && $_SESSION["logado"] == true) {
        header("Location: menuPrincipal.php");
    }

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        require_once("conexao.php");
        
        $conexao = conectadb();
        $conexao->set_charset("utf8");

        if (empty($_POST["nome"])) {
            $erroNome = "Favor digitar o seu nome";
        }
        else {
            $nome = $_POST["nome"];
        }

        if (empty($_POST["cpf"])) {
            $erroCpf = "Favor digitar o seu CPF";
        }
        elseif (strlen($_POST["cpf"]) < 14) {
            $erroCpf = "Favor digitar o CPF inteiro";
        }
        else {
            $sql = "SELECT id FROM usuarios WHERE cpf = ?";

            if ($stmt = $conexao->prepare($sql)) {
                $stmt->bind_param("s", $_POST["cpf"]);

                if ($stmt->execute()) {
                    $stmt->store_result();

                    if ($stmt->num_rows == 1) {
                        $erroCpf = "Este CPF já foi usado";
                    }
                    else {
                        $cpf = $_POST["cpf"];
                    }
                }
                $stmt->close();
            }
        }

        if (empty($_POST["email"])) {
            $erroEmail = "Favor digitar o seu E-Mail";
        }
        else {
            $email = $_POST["email"];
        }

        if (empty($_POST["senha"])) {
            $erroSenha = "Favor digitar uma senha";
        }
        elseif (strlen($_POST["senha"]) < 6) {
            $erroSenha = "Senha deve ter pelo menos 6 caracteres";
        }
        else {
            $senha = $_POST["senha"];
        }

        if (empty($_POST["confirmaSenha"])) {
            $erroConfirmaSenha = "Favor confirmar a senha";
        }
        else {
            if (empty($erroSenha) && $senha != $_POST["confirmaSenha"]) {
                echo $senha;
                echo $confirmaSenha;
                $erroConfirmaSenha = "Senhas diferentes";
            }
            else {
                $confirmaSenha = $_POST["confirmaSenha"];
            }
        }

        if (empty($erroNome) && empty($erroCpf) && empty($erroEmail) && empty($erroSenha) && empty($erroConfirmaSenha)) {
            $sql = "INSERT INTO usuarios (nome, cpf, email, senha) VALUES (?,?,?, MD5(?))";

            if ($stmt = $conexao->prepare($sql)) {
                $stmt->bind_param("ssss", $nome, $cpf, $email, $senha);

                if ($stmt->execute()) {
                    $mensagemRegistro = "Registrado com sucesso!";
                }
                else {
                    $mensagemRegistro = "Erro: $stmt->error";
                }
                $stmt->close();
            }
        }
        $conexao->close();
    }
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
    <script type="text/javascript" src="mascara.js"></script>

    <title>Cadastro - Sistema Emprestimos</title>

</head>
<body>
    <div class="container-fluid bg-dark vh-100 overflow-auto">
        <div class="row">
            <div class="col-10 col-sm-4 mx-auto mt-3">
                <form class="form-control" action="" method="POST">
                    <div class="row mb-3 mx-3">
                        <label class="form-label">Nome</label>
                        <input class="form-control" type="text" name="nome" value="<?=$nome?>" onkeydown="javascript:fMasc(this, mDig);">
                        <span class="text-danger"><?php echo $erroNome;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <label class="form-label">CPF</label>
                        <input class="form-control" type="text" name="cpf" value="<?=$cpf?>" onkeydown="javascript:fMasc(this, mCPF);" maxlength="14">
                        <span class="text-danger"><?php echo $erroCpf;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <label class="form-label">E-Mail</label>
                        <input class="form-control" type="text" name="email" value="<?=$email?>">
                        <span class="text-danger"><?php echo $erroEmail;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <label class="form-label">Senha:</label>
                        <input class="form-control" type="password" name="senha" value="<?=$senha?>">
                        <span class="text-danger"><?php echo $erroSenha;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <label class="form-label">Confirmar Senha:</label>
                        <input class="form-control" type="password" name="confirmaSenha" value="<?=$confirmaSenha?>">
                        <span class="text-danger"><?php echo $erroConfirmaSenha;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <input class="btn btn-success" type="submit" value="Fazer Cadastro">
                        <span class="text-danger"><?php echo $mensagemRegistro?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <span class="text-end">
                            Já tem conta? Faça login <a href="login.php">aqui</a>
                        </span>
                    </div>
                </form>
                
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</body>
</html>