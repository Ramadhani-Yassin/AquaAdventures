<?php
session_start();
error_reporting(0);
include('includes/config.php');
include('firebase/firebase.php');
include('firebase/push.php');

$firebase = new Firebase();
$push = new Push();

// optional payload
$payload = array();
$payload['name'] = 'Aqua Adventure';
$payload['version'] = '1.6';
$push->setTitle("Your Booking Status");
$push->setImage('');
$push->setIsBackground(FALSE);
$push->setPayload($payload);

if(strlen($_SESSION['alogin'])==0) {
    header('location:index.php');
}
else {
    global $firebase_token;
    if(isset($_REQUEST['mobile'])){
        $mobile=$_GET['mobile'];
        $sql = "SELECT firebase_token FROM users WHERE mobile=:mobile";
        $stmt = $dbh->prepare($sql);
        $stmt->bindParam("mobile", $mobile,PDO::PARAM_STR);
        $stmt->execute();
        $firebase_token = $stmt->fetch(PDO::FETCH_OBJ);
        $regId = $firebase_token->firebase_token;
    }

    if(isset($_REQUEST['confirmid'])) {
        $eid=intval($_GET['confirmid']);
        $status='Confirmed';
        $sql = "UPDATE orders SET status=:status WHERE id=:eid";
        $query = $dbh->prepare($sql);
        $query -> bindParam(':status',$status, PDO::PARAM_STR);
        $query-> bindParam(':eid',$eid, PDO::PARAM_STR);
        $query -> execute();
        $push->setMessage("Your tour booking has been confirmed. We will contact you soon.");
        $json = $push->getPush();
        $response = $firebase->send($regId, $json);
        $msg="Status Updated Successfully";
    }
    
    if(isset($_REQUEST['prepareid'])) {
        $eid=intval($_GET['prepareid']);
        $status='Preparing';
        $sql = "UPDATE orders SET status=:status WHERE id=:eid";
        $query = $dbh->prepare($sql);
        $query -> bindParam(':status',$status, PDO::PARAM_STR);
        $query-> bindParam(':eid',$eid, PDO::PARAM_STR);
        $query -> execute();
        $push->setMessage("Your boat is being prepared for the tour");
        $json = $push->getPush();
        $response = $firebase->send($regId, $json);
        $msg="Status Updated Successfully";
    }
    
    if(isset($_REQUEST['wayid'])) {
        $eid=intval($_GET['wayid']);
        $status='On Way';
        $sql = "UPDATE orders SET status=:status WHERE id=:eid";
        $query = $dbh->prepare($sql);
        $query -> bindParam(':status',$status, PDO::PARAM_STR);
        $query-> bindParam(':eid',$eid, PDO::PARAM_STR);
        $query -> execute();
        $push->setMessage("Your boat is on the way to the pickup location");
        $json = $push->getPush();
        $response = $firebase->send($regId, $json);
        $msg="Status Updated Successfully";
    }
    
    if(isset($_REQUEST['deliveredid'])) {
        $eid=intval($_GET['deliveredid']);
        $status='Completed';
        $sql = "UPDATE orders SET status=:status WHERE id=:eid";
        $query = $dbh->prepare($sql);
        $query -> bindParam(':status',$status, PDO::PARAM_STR);
        $query-> bindParam(':eid',$eid, PDO::PARAM_STR);
        $query -> execute();
        $push->setMessage("Your tour has been completed successfully. Thank you for choosing Aqua Adventure!");
        $json = $push->getPush();
        $response = $firebase->send($regId, $json);
        $msg="Status Updated Successfully";
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
    <title>Aqua Adventure | Manage Bookings</title>

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
            background: #dd3d36;
            color:#fff;
            -webkit-box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
            box-shadow: 0 1px 1px 0 rgba(0,0,0,.1);
        }
        .succWrap{
            padding: 10px;
            margin: 0 0 20px 0;
            background: #5cb85c;
            color:#fff;
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
                        <h2 class="page-title"><a href="manage-bookings.php">Manage Bookings</a></h2>

                        <!-- Zero Configuration Table -->
                        <div class="panel panel-default">
                            <div class="panel-heading">Bookings List</div>
                            <div class="panel-body">
                                <?php if($error){?><div class="errorWrap"><strong>ERROR</strong>:<?php echo htmlentities($error); ?> </div><?php }
                                else if($msg){?><div class="succWrap"><strong>SUCCESS</strong>:<?php echo htmlentities($msg); ?> </div><?php }?>
                                <table id="zctb" class="display table table-striped table-bordered table-hover" cellspacing="0" width="100%">
                                    <thead>
                                        <tr>
                                            <th>Booking ID</th>
                                            <th>Name</th>
                                            <th>Mobile Number</th>
                                            <th>Email</th>
                                            <th>Hotel/Address</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <?php $sql = "SELECT orders.*, users.name, users.mobile, users.email,users.address from orders INNER JOIN users on orders.user_id=users.id order by id";
                                        $query = $dbh -> prepare($sql);
                                        $query->execute();
                                        $results=$query->fetchAll(PDO::FETCH_OBJ);
                                        $cnt=1;
                                        if($query->rowCount() > 0) {
                                        foreach($results as $result) { ?>
                                        <tr>
                                            <td><?php echo htmlentities($result->id);?></td>
                                            <td><?php echo htmlentities($result->name);?> <?php echo htmlentities($result->lname);?></td>
                                            <td><?php echo htmlentities($result->mobile);?></td>
                                            <td><?php echo htmlentities($result->email);?></td>
                                            <td><?php echo htmlentities($result->address);?></td>
                                            <td><b class="text-warning"><?php echo htmlentities($result->status);?></b></td>
                                            <td><a href="view-order.php?orderid=<?php echo $result->id;?>" >View Booking</a></td>
                                            <td>
                                                <select onchange="location = this.value;" value=<?php echo htmlentities($result->status);?>>
                                                    <!--<option <?php if ($result->status == 'Confirmed') echo 'selected'; ?> >Pending</option>-->
                                                    <option value="manage-bookings.php?confirmid=<?php echo $result->id;?>&mobile=<?php echo $result->mobile;?>"
                                                    <?php if ($result->status == 'Confirmed') echo 'selected'; ?> >Confirmed</option>
                                                    <option value="manage-bookings.php?prepareid=<?php echo $result->id;?>&mobile=<?php echo $result->mobile;?>"
                                                    <?php if ($result->status == 'Prepared') echo 'selected'; ?>>Boat Prepared</option>
                                                    <option value="manage-bookings.php?wayid=<?php echo $result->id;?>&mobile=<?php echo $result->mobile;?>"
                                                    <?php if ($result->status == 'On Way') echo 'selected'; ?>>On Route</option>
                                                    <option value="manage-bookings.php?deliveredid=<?php echo $result->id;?>&mobile=<?php echo $result->mobile;?>"
                                                    <?php if ($result->status == 'Completed') echo ' selected'; ?>>Completed</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <?php $cnt=$cnt+1; }} ?>
                                    </tbody>
                                </table>
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
    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function() {
                $('.succWrap').slideUp("slow");
            }, 3000);
        });
    </script>
</body>
</html>
<?php } ?>