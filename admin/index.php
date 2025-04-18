<?php
session_start();
include('includes/config.php');

//   Author: Quintus Labs
//   Author URL: http://quintuslabs.com
//   date: 12/11/2019
//   Github URL: https://github.com/quintuslabs/GroceryStore-with-server/
//password: 21232f297a57a5a743894a0e4a801fc3



if(isset($_POST['login']))

{

$email=$_POST['username'];

$password=md5($_POST['password']);

$sql ="SELECT UserName,Password FROM admin WHERE UserName=:email and Password=:password";

$query= $dbh -> prepare($sql);

$query-> bindParam(':email', $email, PDO::PARAM_STR);

$query-> bindParam(':password', $password, PDO::PARAM_STR);

$query-> execute();

$results=$query->fetchAll(PDO::FETCH_OBJ);

if($query->rowCount() > 0)

{

$_SESSION['alogin']=$_POST['username'];

echo "<script type='text/javascript'> document.location = 'dashboard.php'; </script>";

} else{

  

  echo "<script>alert('Invalid Details');</script>";



}



}

?>

<!doctype html>
<html lang="en" class="no-js">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
	<meta name="description" content="">
	<meta name="author" content="">
	<link rel="icon" href="img/logo.png" type="image/gif" sizes="16x16">
	
	<link rel="stylesheet" href="css/font-awesome.min.css">
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/dataTables.bootstrap.min.css">
	<link rel="stylesheet" href="css/bootstrap-social.css">
	<link rel="stylesheet" href="css/bootstrap-select.css">
	<link rel="stylesheet" href="css/fileinput.min.css">
	<link rel="stylesheet" href="css/awesome-bootstrap-checkbox.css">
	<link rel="stylesheet" href="css/style.css">
	<style>
		.login-page {
			background: linear-gradient(135deg, #1e90ff 0%, #00bfff 50%, #87cefa 100%);
			background-attachment: fixed;
			min-height: 100vh;
			padding: 20px 0;
		}
		.well {
			border-radius: 8px;
			box-shadow: 0 4px 20px rgba(0,0,0,0.15);
			border: none;
		}
		.btn-primary {
			background-color: #1e90ff;
			border: none;
			padding: 10px;
			font-weight: 600;
			transition: all 0.3s;
		}
		.btn-primary:hover {
			background-color: #0066cc;
			transform: translateY(-2px);
			box-shadow: 0 4px 8px rgba(0,0,0,0.2);
		}
		.form-control {
			border-radius: 4px;
			padding: 12px 15px;
			border: 1px solid #ddd;
		}
	</style>
</head>

<body>
	<div class="login-page">
		<div class="form-content">
			<div class="container">
				<div class="row">
					<div class="col-md-6 col-md-offset-3">
						<h3 class="text-center text-bold text-light mt-4x" style="text-shadow: 0 2px 4px rgba(0,0,0,0.3);">Admin Login</h3>
						<div class="well row pt-2x pb-3x" style="background-color: rgba(255,255,255,0.95);">
							<div class="col-md-8 col-md-offset-2">
								<div class="text-center">
									<img src="../assets/images/logo.png" style="height:60px; margin-bottom: 20px; "/>
								</div>
								
								<form method="post">
									<label for="" class="text-uppercase text-sm">Your Username </label>
									<input type="text" placeholder="Username" name="username" class="form-control mb">

									<label for="" class="text-uppercase text-sm">Password</label>
									<input type="password" placeholder="Password" name="password" class="form-control mb">
									<button class="btn btn-primary btn-block" name="login" type="submit">LOGIN</button>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<!-- Loading Scripts -->
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap-select.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/Chart.min.js"></script>
	<script src="js/fileinput.js"></script>
	<script src="js/chartData.js"></script>
	<script src="js/main.js"></script>
</body>
</html>