<?php
    session_start();

    if (!(isset($_SESSION["logado"]) && $_SESSION["logado"] == true)) {
        header("Location: login.php");
    }
?>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">

    <title>Cancelar Reserva - Sistema Emprestimos</title>
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <button class="navbar-toggler ri-menu-line" type="button" data-bs-toggle="collapse" href="#sidebar" aria-expanded="false" aria-controls="sidebar"></button>
        
            <h1 class="navbar-brand position-absolute start-50 translate-middle-x">Sistema Empréstimos</h1>
            
            <a class="navbar-toggler text-decoration-none" href="logout.php">
                <i class="fs-4 ri-logout-box-line"></i>
            </a>
        </div>
    </nav>
    <div class="container-fluid">
        <div class="row flex-nowrap">
            <div class="col-auto bg-dark vh-100 collapse collapse-horizontal" id="sidebar">
                <a href="menuPrincipal.php" class="nav-link px-0">
                    <i class="fs-4 ri-home-2-line" style="vertical-align: middle;"></i>
                    <span class="align-middle">Menu Principal</span>
                </a>
                <a href="listarEmprestimos.php" class="nav-link px-0">
                    <i class="fs-4 ri-table-line" style="vertical-align: middle;"></i>
                    <span class="align-middle">Meus Empréstimos</span>
                </a>
                <div class="nav-link px-0">
                    <button class="btn btn-link text-decoration-none shadow-none p-0 m-0" data-bs-toggle="collapse" data-bs-target="#reserva-colapso" aria-expanded="false">
                        <i class="fs-4 ri-calendar-check-line" style="vertical-align: middle;"></i>
                        <span class="align-middle">Reservas</span>
                        <i class="fs-4 ri-arrow-drop-down-line" style="vertical-align: middle;"></i>
                    </button>
                    <div class="collapse" id="reserva-colapso">
                        <ul class="list-group-flush list-unstyled mb-0 ps-4">
                            <li class="mb-2">
                                <a class="text-decoration-none ps-0" href="listarReservas.php">Minhas Reservas</a>
                            </li>
                            <li>
                                <a class="text-decoration-none ps-0" href="fazerReserva.php">Fazer Reserva</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <a href="listarMateriais.php" class="nav-link px-0">
                    <i class="fs-4 ri-projector-2-line" style="vertical-align: middle;"></i>
                    <span class="align-middle">Materiais</span>
                </a>
            </div>
            <div class="col bg-light vh-100 overflow-auto">
                <?php
                    if (isset($_GET['id'])) {
                        require_once("conexao.php");

                        $conexao = conectadb();
                        $conexao->set_charset("utf8");

                        $id = $_GET['id'];

                        $sql = "UPDATE reservas SET status='Cancelado' WHERE id = $id";

                        $stmt = $conexao->prepare($sql);

                        if($stmt->execute()) {
                            echo "<h1 class='text-center'>Reserva cancelada com sucesso!</h1>";
                        }
                        else {
                            echo $stmt->error;
                        }

                        $conexao->close();
                    }
                    else {
                        echo "<h1 class='text-center'>Erro: Acesso sem ID.</h1>";
                    }

                    
                ?>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</body>
</html>