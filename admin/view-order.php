<?php
session_start();
error_reporting(0);
include('includes/config.php');
if(strlen($_SESSION['alogin'])==0)
{	
header('location:index.php');
}
else{
if(isset($_REQUEST['orderid']))
{
$orderid=intval($_GET['orderid']);
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
    <meta name="theme-color" content="#3e454c">
    <link rel="icon" href="img/logo.png" type="image/gif" sizes="16x16">
    <title>GS | View Order  
    </title>
    <!-- Font awesome -->
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <!-- Sandstone Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- Bootstrap Datatables -->
    <link rel="stylesheet" href="css/dataTables.bootstrap.min.css">
    <!-- Bootstrap social button library -->
    <link rel="stylesheet" href="css/bootstrap-social.css">
    <!-- Bootstrap select -->
    <link rel="stylesheet" href="css/bootstrap-select.css">
    <!-- Bootstrap file input -->
    <link rel="stylesheet" href="css/fileinput.min.css">
    <!-- Awesome Bootstrap checkbox -->
    <link rel="stylesheet" href="css/awesome-bootstrap-checkbox.css">

    <!-- Admin Stye -->
    <link rel="stylesheet" href="css/style.css">
    <style>
      .errorWrap {
        padding: 10px;
        margin: 0 0 20px 0;
        background: #fff;
        border-left: 4px solid #dd3d36;
        -webkit-box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
        box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
      }
      .succWrap{
        padding: 10px;
        margin: 0 0 20px 0;
        background: #fff;
        border-left: 4px solid #5cb85c;
        -webkit-box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
        box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
      }
    </style>
  </head>
  <body>
    <?php include('includes/header.php');?>
    <div class="ts-main-content">
      <?php include('includes/leftbar.php');?>
      <div class="content-wrapper">
        <div class="container-fluid">
          <div class="row">
            <div class="col-md-12">
              <div class="row">
                <div class="col-md-3 h5">
                  <a href="manage-bookings.php"> GO BACK
                  </a>
                </div>
                <div class="col-md-3 h5">
                </div>
                <div class="col-md-3 h5">
                </div>
                <div class="col-md-3 h5">
                  <b>Order No. : 
                    <?php echo htmlentities($orderid); ?>
                  </b>
                </div>	
              </div>
              <!-- Zero Configuration Table -->
              <div class="panel panel-default">
                <div class="panel-heading">Order List
                </div>
                <div class="panel-body">
                  <div class="container">
                    <div class="row">
                      <?php $sql = "SELECT orders.*, users.name, users.mobile, users.email,users.address from orders INNER JOIN users on orders.user_id=users.id AND  orders.id=".$orderid;
                        $query = $dbh -> prepare($sql);
                        $query->execute();
                        $result=$query->fetch(PDO::FETCH_OBJ);
                        ?>
                      <div class="col-md-3 h5">
                        <b>Name :
                        </b> 
                        <?php echo htmlentities($result->name); ?> 
                      </div>
                      <div class="col-md-3 h5">
                        <b>Phone :
                        </b> 
                        <?php echo htmlentities($result->mobile); ?>
                      </div>
                      <div class="col-md-3 h5">
                        <b>Email :
                        </b> 
                        <?php echo htmlentities($result->email); ?>
                      </div>
                      </div>
                      <div class="row">
                        <div class="col-md-4 h5">
                          <b>Address :
                          </b> 
                          <?php echo htmlentities($result->address); ?>
                        </div>	
                        <div class="col-md-4 h5">
                          <b>Pickup from store :
                          </b> 
                          <?php if($result->store_pickup==1){?>
                           <b>Yes</b>
                           <?php }else{?>
                            <b>Home Delivery</b>
                            <?php  }?>
                        </div>	
                    </div>
                  </div>
                  <br>
                  <table class="display table table-striped table-bordered table-hover" cellspacing="0" width="100%">
                    <thead>
                      <tr>
                        <th>#
                        </th>
                        <th>Name
                        </th>
                        <th>Quantity
                        </th>
                       
                      </tr>
                    </thead>
                    <tbody>
                      <?php   $sql = "SELECT * from orderlist WHERE order_id=".$orderid;

                      $query = $dbh -> prepare($sql);
                      $query->execute();
                      $results=$query->fetchAll(PDO::FETCH_OBJ);
                      $cnt=1;
                      if($query->rowCount() > 0)
                      {
                      foreach($results as $result)
                      {				?>	
                      <tr>
                        <td>
                          <?php echo htmlentities($cnt);?>
                        </td>
                        <td>
                          <?php echo htmlentities($result->itemName);?> 
                        </td>
                        <td>
                          <?php echo htmlentities($result->itemQuantity);?> (<?php echo htmlentities($result->attribute);?>)
                        </td>
                       
                      </tr>
                      <?php $cnt=$cnt+1; }} ?>	
                    </tbody>
                  </table>
                  <div class="container">
                    <div class="row">
                      <div class="col-md-8">
                      </div>
                      <?php 
                        $sqlgd = "SELECT * from orders WHERE id=".$orderid;
                        $querygd = $dbh -> prepare($sqlgd);
                        $querygd->execute();
                        $resultgd=$querygd->fetch(PDO::FETCH_OBJ);
                        ?>
                      <div class="col-md-4 h4">
                        <!-- <b>Grand Total : 
                          RS. <?php echo htmlentities($resultgd->total);?>
                        </b> -->
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Loading Scripts -->
    <script src="js/jquery.min.js">
    </script>
    <script src="js/bootstrap-select.min.js">
    </script>
    <script src="js/bootstrap.min.js">
    </script>
    <script src="js/jquery.dataTables.min.js">
    </script>
    <script src="js/dataTables.bootstrap.min.js">
    </script>
    <script src="js/Chart.min.js">
    </script>
    <script src="js/fileinput.js">
    </script>
    <script src="js/chartData.js">
    </script>
    <script src="js/main.js">
    </script>
  </body>
</html>
<?php } ?>