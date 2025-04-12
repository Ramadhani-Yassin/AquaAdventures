<?php
session_start();
error_reporting(0);
include('includes/config.php');

  

if(strlen($_SESSION['alogin'])==0)
	{	
header('location:index.php');
}
    $category_id = $_POST["category_id"];

    $sql = "SELECT * FROM sub_categories where category_id=".$category_id;
    $query = $dbh -> prepare($sql);
    $query->execute();
    $results=$query->fetchAll(PDO::FETCH_OBJ); 

    ?>
    <option value="">Select SubCategory</option>
    <?php

    if($query->rowCount() > 0)
                                {
                                foreach($results as $result)
                                {				?>	
                                <option value="<?php echo htmlentities($result->id);?>"><?php echo htmlentities($result->sub_category_title);?></option>
                                <?php }} ?>

?>