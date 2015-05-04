<?php

class database {
    private $db;
    function __construct() {
        require_once 'connection.php';
        // connecting to database
        $this->db = new connection();
        $this->db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    //create new user
    public function storeUser($name, $email, $password) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015","theaeyop_accounts");
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $reputation = 0;
        $result = mysqli_query($conn, "INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at, reputation_total, reputation_avg) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW(), '$reputation', '$reputation')");
        // check for successful store
        if ($result) {
            // get user details 
            $uid = mysqli_insert_id($conn); // last inserted id
            $result = mysqli_query($conn, "SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysqli_fetch_array($result);
        } else {
            return false;
        }
    }

    //retrieve user
    public function getUserByEmailAndPassword($email, $password) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015","theaeyop_accounts");
        $result = mysqli_query($conn, "SELECT * FROM users WHERE email = '$email'");
        // check for result 
        $no_of_rows = mysqli_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysqli_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user auth details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    public function getEmailByUniqueID($unique_id) {
        if ($unique_id == 0) { //premature case where we are checking the buyer_id of 0 from a transaction that has not "begun" yet
            return false;
        }
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015","theaeyop_accounts");
        $result = mysqli_query($conn, "SELECT * FROM users WHERE unique_id = '$unique_id'");
        $no_of_rows = mysqli_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysqli_fetch_array($result);
            $email = $result['email'];
            return $email;
        }
        else {
            //name not found
            return false;
        }
    }

    // does user exist?
    public function isUserExisted($email) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015","theaeyop_accounts");
        $result = mysqli_query($conn, "SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysqli_num_rows($result);
        if (!$result || mysqli_num_rows($result) > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    //encrypt password
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    //check hash
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

    //sell book
    public function sellBook($seller_id, $price, $isbn, $bcondition, $author, $title, $image_url) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015","theaeyop_accounts");
        $uuid = uniqid('transaction_', true);
        $title = mysqli_real_escape_string($conn, $title);
        $result = mysqli_query($conn, "INSERT INTO transactions(unique_id, seller_id, isbn, author, title, image_url, bcondition, price, created_at) VALUES('$uuid', '$seller_id', '$isbn', '$author', '$title', '$image_url', '$bcondition', '$price', NOW())") or die(mysqli_error($conn));
        if ($result) { //we successfully started a new sale;
            $tid = mysqli_insert_id($conn); // last inserted id
            $result = mysqli_query($conn, "SELECT * FROM transactions WHERE tid = $tid");
            // return user details
            return mysqli_fetch_array($result);
        }
        else {
            return false;
        }

    }

    public function getBook($search_type, $string_query, $bcondition) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015", "theaeyop_accounts");
        $search_string = "%" . $string_query;
        $search_string .= "%";
        $search_string = mysqli_real_escape_string($conn, $search_string);
        
        switch ($search_type) {
	    case 'Author':
	        if ($bcondition != 'Any') {
	        $result = mysqli_query($conn, "SELECT * FROM transactions WHERE author LIKE '$search_string' AND bcondition = '$bcondition'") or die(mysqli_error($conn));
	        }
	        else {
	       	$result = mysqli_query($conn, "SELECT * FROM transactions WHERE author LIKE '$search_string'") or die(mysqli_error($conn));
	        }
	        break;
	    case 'Title':
	        if ($bcondition != 'Any') {
	        $result = mysqli_query($conn, "SELECT * FROM transactions WHERE title LIKE '$search_string' AND bcondition = '$bcondition'") or 	 die(mysqli_error($conn));
	        }
	        else {
	       	$result = mysqli_query($conn, "SELECT * FROM transactions WHERE title LIKE '$search_string'") or die(mysqli_error($conn));
	        }
	        break;
	    case 'ISBN':
	        if ($bcondition != 'Any') {
	        $result = mysqli_query($conn, "SELECT * FROM transactions WHERE isbn LIKE '$search_string' AND bcondition = '$bcondition'") or die(mysqli_error($conn));
	        }
	        else {
	       	$result = mysqli_query($conn, "SELECT * FROM transactions WHERE isbn LIKE '$search_string'") or die(mysqli_error($conn));
	        }
	        break;
	}
        
        
        // check for result 
        $no_of_rows = mysqli_num_rows($result);
        $result_array = array();
        $index = 0;
        if ($no_of_rows > 0) {
		while($row = mysqli_fetch_assoc($result))
		{
     			$result_array[$index] = $row;
     			$index++;
		}
		return $result_array;
        }
        else {
            return false;
        }

    }
    
    ////////////
    
    public function sellerBooks($unique_id) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015", "theaeyop_accounts");
        $search_string = $unique_id;
        $search_string = mysqli_real_escape_string($conn, $search_string);
        
        $result = mysqli_query($conn, "SELECT * FROM transactions WHERE seller_id = '$search_string'") or die(mysqli_error($conn));
        
        
        // check for result 
        $no_of_rows = mysqli_num_rows($result);
        $result_array = array();
        $index = 0;
        if ($no_of_rows > 0) {
		while($row = mysqli_fetch_assoc($result))
		{
     			$result_array[$index] = $row;
     			$index++;
		}
		return $result_array;
        }
        else {
            return false;
        }

    }
     
    ////////////

    
    public function getSellNotifications($unique_id) {
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015", "theaeyop_accounts");
        $result = mysqli_query($conn, "SELECT * FROM transactions WHERE seller_id = '$unique_id'") or die(mysqli_error($conn));
        $no_of_rows = mysqli_num_rows($result);

        $result_array = array();
        $index = 0;
        if ($no_of_rows > 0) {
        while($row = mysqli_fetch_assoc($result))
        {
                $result_array[$index] = $row;
                $index++;
        }
        return $result_array;
        }
        else {
            return false;
        }
    }

    //takes in the buyer ID AND transaction id and updates the table - will show "Transaction Started" : updated_at time converted :)
    public function confirmTransaction($transaction_id, $buyer_id) { //renamed to transaction id to make more sense -> this unique id is the transaction_XXXLCKASLDMA ID! not the user unique_id
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015", "theaeyop_accounts");
        $result = mysqli_query($conn, "UPDATE transactions SET buyer_id = '$buyer_id', transaction_status = '1', updated_at = NOW() WHERE unique_id = '$transaction_id'") or die(mysqli_error($conn));  
        $select_result = mysqli_query($conn, "SELECT * FROM transactions WHERE unique_id = '$transaction_id'") or die(mysqli_error($conn));  
        
        if ($select_result) {
            $select_result = mysqli_fetch_array($select_result);
            $seller_unique_id = $select_result['seller_id'];
            $email = $this->getEmailByUniqueID($seller_unique_id);
            return $email;
        }  
        else {
            return false;
        }
    }

    public function completeTransaction($transaction_id) { //updated_at will now show "Transaction Completed" : updated_at time converted :)
        $conn = mysqli_connect("localhost", "theaeyop", "bookit2015", "theaeyop_accounts");
        $result = mysqli_query($conn, "UPDATE transactions SET transaction_status = '2', updated_at = NOW() WHERE unique_id = '$transaction_id'") or die(mysqli_error($conn));  
        if ($result) {
            return true;
        }  
        else {
            return false;
        }
    }


}

?>