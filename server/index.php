<?php

//ubooks android backend
//written by michael romaine
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];

    require_once 'include/database.php';
    //require_once 'include/SMTP_validateEmail.class.php';
    $db = new database();
    //$SMTP_Validator = new SMTP_validateEmail();

    // response array
    $response = array("tag" => $tag, "error" => FALSE);

    // check for tag type
    if ($tag == 'login') {
        // Request type is logging in
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            $response["user"]["reputation_avg"] = $user["reputation_avg"];
            $response["user"]["reputation_total"] = $user["reputation_avg"];
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = TRUE;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        list($email_name, $email_domain) = explode('@', $email);
        $email_domain = strtolower($email_domain);

        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = TRUE;
            $response["error_msg"] = "User already exists.";
            echo json_encode($response);
        } 
        elseif ($email_domain != 'knights.ucf.edu' && $email_domain != 'ucf.edu') {
            $response["error"] = TRUE;
            $response["error_msg"] = "Your e-mail needs to be a UCF one.";
            echo json_encode($response);
        }
        else {
            // store user
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // user stored successfully
                $response["error"] = FALSE;
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = TRUE;
                $response["error_msg"] = "Error occured in registering.";
                echo json_encode($response);
            }
        }
    } else if ($tag == 'getuser') {
        $unique_id = $_POST["unique_id"];
        $email = $db->getEmailByUniqueID($unique_id);
        if ($email) {
            $response["error"] = FALSE;
            $response["email"] = $email;
            echo json_encode($response);
        }
        else {
            //CHECK IN ANDROID, GETUSER WILL RETURN AN ERROR AS TRUE IF NO NAME CORRESPONDS TO THAT ID (IE WHEN YOU CHECK A BUYER_ID IN THE TRANSACTIONS TABLE BEFORE IT
            //IT IS POPULATED)
            $response["error"] = TRUE;
            $response["email"] = "No one yet!";
            echo json_encode($response);
        }
    } else if ($tag == 'sell') {
        $seller_id = $_POST['seller_id'];
        $price = $_POST['price'];
        $isbn = $_POST['isbn'];
        $bcondition = $_POST['bcondition'];
        $author = $_POST['author'];
        $title = $_POST['title'];
        $image_url = $_POST['image_url'];
        $sale = $db->sellBook($seller_id, $price, $isbn, $bcondition, $author, $title, $image_url);
        if ($sale) {
            $response["error"] = FALSE;
            $response["transaction_id"] = $sale["unique_id"];
            $response["seller_id"] = $sale["seller_id"];
            $response["isbn"] = $sale["isbn"];
            $response["bcondition"] = $sale["bcondition"];
            $response["price"] = $sale["price"];
            $response["transaction_status"] = $sale["transaction_status"];
            $response["created_at"] = $sale["created_at"];
            $response["title"] = $sale["title"];
            $response["author"] = $sale["author"];
            $response["image_url"] = $sale["image_url"];
            echo json_encode($response);
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "Error occured in starting a sale.";
            echo json_encode($response);
        }

    } else if ($tag == 'buysearch') {
        //idk if there's any syntax issues with this code but I'm pretty sure that if any errors happen it's on this side
        //when you successfully tag 'getnotifications' you can do a json request to a function and return the string of the name in the database (getNameByUniqueID tag is above)
        //anything else I need to work on will be after finals
    	$search_type = $_POST["search_type"]; //no posting
        $string_query = $_POST["string_query"];
        $bcondition = $_POST["bcondition"];
        if (!empty($string_query)) {
            $buysearch = $db->getBook($search_type, $string_query, $bcondition);

            if ($buysearch) {
                echo json_encode(array('error' => FALSE, 'data' => $buysearch));
            }
            else {
                $response["error"] = TRUE;
                $response["error_msg"] = "No books found.";
                echo json_encode($response);
            }
        } 
    } else if ($tag == 'displaysale') {
        //idk if there's any syntax issues with this code but I'm pretty sure that if any errors happen it's on this side
        //when you successfully tag 'getnotifications' you can do a json request to a function and return the string of the name in the database (getNameByUniqueID tag isabove)
        //anything else I need to work on will be after finals
    	$unique_id = $_POST["unique_id"];
        if (!empty($unique_id)) {
            $bookList = $db->sellerBooks($unique_id);

            if ($bookList) {
                echo json_encode(array('error' => FALSE, 'data' => $bookList));
            }
            else {
                $response["error"] = TRUE;
                $response["error_msg"] = "No books found.";
                echo json_encode($response);
            }
        } 
    } else if ($tag = 'buyconfirm') {
        $transaction_id = $_POST["transaction_id"]; //unique_id of TRANSACTION not user
        $buyer_id = $_POST["buyer_id"]; //unique_id of BUYER user
        $buyconfirm = $db->confirmTransaction($transaction_id, $buyer_id);
        if ($buyconfirm) {
            $response["error"] = FALSE;
            $response["email"] = $buyconfirm; //I didn't know what to say here lol, change it as you wish
            echo json_encode($response);
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "Some shit went wrong. Whoops. Check the syntax? :)";
            echo json_encode($response);
        }
    } else if ($tag == 'buycomplete') {
        $transaction_id = $_POST["transaction_id"];
        $buycomplete = $db->completeTransaction($transaction_id);
        if ($buycomplete) {
            $response["error"] = FALSE;
            $response["message"] = "Successfully completed transaction!"; //I didn't know what to say here lol, change it as you wish
            echo json_encode($response);
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "Some shit went wrong. I'm studying right now so it's probably because I rushed in typing this.";
            echo json_encode($response);
        }

    } else if ($tag == 'displaynotifications') {
        $unique_id = $_POST["unique_id"]; //no posting
        $notifications = $db->getSellNotifications($unique_id); //are we going to get notifications as a buyer/seller separately or only show seller notifications?
        //if we want separate notofications, we need to make a getBuyNotifications with the exact same logic but showing by buyer_id
        if ($notifications) {
            echo json_encode(array('error' => FALSE, 'data' => $notifications));
        }
        else {
            $response["error"] = TRUE;
            $response["error_msg"] = "No notifications yet.";
            echo json_encode($response);
        }
    }
    else {
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown 'tag' value. No sql injection for you. :)";
        echo json_encode($response);
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Some required parameters are missing. Please use the UBooks app to actually login or sign up.";
    echo json_encode($response);
}

?>