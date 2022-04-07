<?php
    session_start();

    if (isset($_SESSION["logado"]) && $_SESSION["logado"] == true) {
        header("Location: menuPrincipal.php");
    }

    $cpf = $senha = "";
    $erroNome = $erroCpf = $erroEmail = $erroSenha = $mensagemRegistro = "";

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        require_once("conexao.php");
        
        $conexao = conectadb();
        $conexao->set_charset("utf8");

        if (empty($_POST["cpf"])) {
            $erroCpf = "Favor digitar o seu CPF";
        }
        else {
            $cpf = $_POST["cpf"];
        }

        if (empty($_POST["senha"])) {
            $erroSenha = "Favor digitar sua senha";
        }
        else {
            $senha = $_POST["senha"];
        }

        if (empty($erroCpf) && empty($erroSenha)) {
            $sql = "SELECT id, nome, cpf, email, senha, funcionario FROM usuarios WHERE cpf = ? AND senha = MD5(?);";

            if ($stmt = $conexao->prepare($sql)) {
                $stmt->bind_param("ss", $cpf, $senha);

                if ($stmt->execute()) {
                    $stmt->store_result();

                    if ($stmt->num_rows == 1) {
                        $stmt->bind_result($idResultado, $nomeResultado, $cpfResultado, $emailResultado, $senhaResultado, $funcionarioResultado);
                        if ($stmt->fetch()) {
                            if (md5($senha) == $senhaResultado) {
                                if (!isset($_SESSION)) {
                                    session_start();
                                }

                                if ($funcionarioResultado == true) {
                                    $mensagemRegistro = "Conta de funcionário detectada, favor usar aplicativo";
                                }
                                else {
                                    $_SESSION["logado"] = true;
                                    $_SESSION["id"] = $idResultado;
                                    $_SESSION["nome"] = $nomeResultado;
                                    $_SESSION["cpf"] = $cpf;
                                    $_SESSION["email"] = $emailResultado;
                                    header("Location: login.php");
                                }
                            }
                            else {
                                $mensagemRegistro = "Usuários ou Senha inválidos";
                            }
                        }
                    }
                    else {
                        $mensagemRegistro = "Usuários ou Senha inválidos";
                    }
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
    <title>Login - Sistema Emprestimos</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
    <script type="text/javascript" src="mascara.js"></script>
</head>
<body>
    <div class="container-fluid bg-dark vh-100 overflow-auto">
        <div class="row">
            <div class="col-10 col-sm-4 mx-auto mt-3">
                <form class="form-control" action="" method="POST">
                    <div class="row mb-3 mx-3">
                        <label class="form-label">CPF</label>
                        <input class="form-control" type="text" name="cpf" value="<?=$cpf?>" onkeydown="javascript:fMasc(this, mCPF);" maxlength="14">
                        <span class="text-danger"><?php echo $erroCpf;?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <label class="form-label">Senha</label>
                        <input class="form-control" type="password" name="senha" value="<?=$senha?>">
                        <span class="text-danger"><?php echo $erroSenha;?></span>
                    </div>
                    
                    <div class="row mb-3 mx-3">
                        <input class="btn btn-success" type="submit" value="Fazer Login">
                        <span class="text-danger"><?php echo $mensagemRegistro?></span>
                    </div>
                    <div class="row mb-3 mx-3">
                        <span class="text-end">
                            Não tem conta? Faça seu cadastro <a href="registrar.php">aqui</a>
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