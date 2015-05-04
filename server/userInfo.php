<?php

    require_once 'include/servsettings.php';
    require_once 'include/database.php';

    $db = new database();
    
    //Replace * in the query with the column names.
    $result = mysql_query("SELECT * FROM users", $db);  
    
    //Create an array
    $json_response = array();
    
    while($row = mysql_fetch_array($result, MYSQL_ASSOC)) {
        $row_array['id'] = $row['uid'];
        $row_array['name'] = $row['name'];
        $row_array['type'] = $row['email'];
        // $row_array['size'] = $row['size'];
        // $row_array['tmp_name'] = $row['tmp_name'];
        
        //push the values in the array
        array_push($json_response,$row_array);
    }

    echo json_encode($json_response);
    
    //Close the database connection
    mysql_close($db);
?>