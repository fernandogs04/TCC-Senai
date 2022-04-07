<?php
    $idUsuario = $idMaterial = $nomeMaterial = $dataRetiro = $numeroDias = "";
    $erroMaterial = $erroData = $erroDias = $mensagemRegistro = "";
    
    session_start();

    if (!(isset($_SESSION["logado"]) && $_SESSION["logado"] == true)) {
        header("Location: login.php");
    }

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        if (empty($_POST["nomeMaterial"])) {
            $erroMaterial = "Favor escolher um material";
        }
        else {
            $nomeMaterial = $_POST["nomeMaterial"];
            $idMaterial = $_POST["idMaterial"];
        }

        if (empty($_POST["dataRetiro"])) {
            $erroData = "Favor escolher a data de retiro";
        }
        else {
            $dataRetiro = $_POST["dataRetiro"];
        }

        if (empty($_POST["numeroDias"])) {
            $erroDias = "Favor escolher os dias a retirar";
        }
        else {
            $numeroDias = $_POST["numeroDias"];
        }

        if (empty($erroMaterial) && empty($erroData) && empty($erroDias)) {
            $diasOcupados = array();
            
            require_once("conexao.php");

            $conexao = conectadb();
            $conexao->set_charset("utf8");

            //Caso não for imutável, no loop de determinar disponibilidade criará uma sequencia de fibonacci com a variavel do for,
            //O que não é desejado
            $dataRet = DateTimeImmutable::createFromFormat('Y-m-d', $dataRetiro);

            //Verificar se os dias já não estão reservados
            $sql = "SELECT   data_retiro, dias
                    FROM     reservas
                    WHERE    data_retiro
                    BETWEEN  DATE('" . date_format($dataRet, "Y-m-d") . "' - INTERVAL 2 MONTH)
                    AND      DATE('" . date_format($dataRet, "Y-m-d") . "' + INTERVAL 2 MONTH)
                    AND      id_material = $idMaterial
                    AND      `status` = 'Aberto'
                    ORDER BY data_retiro  ASC";

            $result = $conexao->query($sql);

            if ($result->num_rows > 0) {
                while ($linha = $result->fetch_assoc()) {
                    //enquanto $a for menor que o numero de dias da nova reserva; $a++
                    for ($a = 0; $a < $numeroDias; $a++) {
                        //Adicionar + $a a data, na primeira vez, $a é 0 e deixara a data intacta
                        //Precisa de uma nova variavel pois dataRet é imutavel
                        $dataRetiroLoop = $dataRet->modify("+ " . $a . "day");
                        
                        //enquanto $b for menor que o numero de dias da reserva antiga; $b++
                        for ($b = 0; $b < $linha["dias"]; $b++) {
                            //Converter datas para DateTime
                            $dataLinha = DateTime::createFromFormat('Y-m-d', $linha["data_retiro"]);

                            //Adicionar + $b a data
                            //Na primeira vez, $b é 0 e deixara a data intacta
                            $dataLinha->modify("+$b day");

                            //Verificar se a data
                            if ($dataRetiroLoop == $dataLinha) {
                                //Adicionar data aos dias ocupados
                                $diasOcupados[] = $dataLinha;
                            }
                        }
                    }
                }
            }
            
            //Se a array de dias ocupados estiver vazia,
            //nao tem nenhum dia ocupado e a reserva pode ser feita
            if (empty($diasOcupados)) {
                //Fazer a reserva
                $sql = "INSERT INTO reservas (id_usuario, id_material, data_retiro, dias, `status`) VALUES (?, ?, ?, ?, 'Aberto')";

                if ($stmt = $conexao->prepare($sql)) {
                    $stmt->bind_param("ssss", $_SESSION["id"], $idMaterial, $dataRetiro, $numeroDias);

                    if ($stmt->execute()) {
                        $mensagemRegistro = "Reserva de $nomeMaterial realizada com sucesso!";
                    }
                    else {
                        $mensagemRegistro = "Erro ao registrar reserva";
                    }
                    $stmt->close();
                }
            }
            else {
                if (count($diasOcupados) == 1) {
                    $mensagemRegistro = "O dia " . date_format($diasOcupados[0], 'd/m') . " já está ocupado";
                }
                else {
                    $mensagemRegistro = "Os dias ";
                    for ($i = 0; $i < count($diasOcupados); $i++) {
                        $mensagemRegistro .= date_format($diasOcupados[$i], 'd/m, ');
                    }
                    $mensagemRegistro .= " já estão ocupados";
                }
            }
            
        }
        $conexao->close();
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

    <title>Fazer Reserva - Sistema Emprestimos</title>
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
                <h1 class="text-center">Fazer Reserva</h1>
                <form class="col-9 col-sm-6 m-auto" action="fazerReserva.php" method="POST">
                    <div class="row mb-sm-3">
                        <label class="col-form-label">Material</label>
                        <div class="col-12 col-sm-8 mb-3 mb-sm-0">
                            <input type="hidden" name="idMaterial" id="inputIdMaterial"/>
                            <input type="text" name="nomeMaterial" class="form-control text-muted" id="inputMaterial" readonly/>
                            <span class="erro"><?=$erroMaterial?></span>
                        </div>
                        <div class="col-12 col-sm-4 mb-3 mb-sm-0">
                            <button type="button" class="form-control btn btn-secondary" data-bs-toggle="modal" data-bs-target="#popupMaterial">Selecionar Material</button>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-form-label">Data de Retiro</label>
                        <div class="col-12">
                            <input type="date" name="dataRetiro" class="form-control">
                            <span class="erro"><?=$erroData?></span>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-form-label">Dias Emprestados</label>
                        <div class="col-12">
                            <input type="number" name="numeroDias" min="1" max="10" class="form-control">
                            <span class="erro"><?=$erroDias?></span>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary">Fazer Reserva</button>
                </form>
                <div class="modal fade" id="popupMaterial" tabindex="-1" aria-labelledby="popupMaterialLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title" id="popupMaterialLabel">Selecionar Material</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <table class="table">
                                    <tr>
                                        <th>Nome</th>
                                        <th>Descrição</th>
                                        <th></th>
                                    </tr>
                                    <?php
                                        require_once("conexao.php");

                                        $conexao = conectadb();
                                        $conexao->set_charset("utf8");
                                        
                                        $sql = "SELECT * FROM materiais";
                                        
                                        $result = $conexao->query($sql);
                                
                                        if ($result->num_rows > 0) {
                                            while ($linha = $result->fetch_assoc()) {
                                                echo "<tr>";
                                                $id = $linha["id"];
                                                echo "<td>" . $linha["nome"]."</td>";
                                                $nome = $linha["nome"];
                                                echo "<td>" . $linha["descricao"]."</td>";
                                                echo "<td>";
                                                echo "<button type='button' class='btn btn-primary' data-bs-dismiss='modal'";
                                                echo 'onclick="';
                                                echo "document.getElementById('inputMaterial').value = '$nome';";
                                                echo "document.getElementById('inputIdMaterial').value = '$id';" . '">';
                                                echo "Selecionar";
                                                echo "</button>";
                                                echo "</td>";
                                                echo "</tr>";
                                            }
                                        }
                                        else {
                                            echo "<tr><td colspan='4' align='center'>Sem resultados</td></tr>";
                                        }
                                        $conexao->close();
                                    ?>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal fade" id="popupMensagemRegistro" tabindex="-1" aria-labelledby="popupMensagemRegistroLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-body">
                                <?=$mensagemRegistro?>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
    <?php
        if (!empty($mensagemRegistro)) {
    ?>
            <script type='text/javascript'>
                var myModal = new bootstrap.Modal(document.getElementById('popupMensagemRegistro'));
                myModal.show();
            </script>
    <?php
        }
    ?>
</body>
</html>