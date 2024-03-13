use CGI;
# Server script for mudsling. Allows servers to advertise the
# fact that they are serving, and for clients to find them.

# stores, real ip, given ip, level, name and port

# if only given ip defined, removes game from list

# if none given returns all from list

# if all given adds it too the list for 500 secons

# Bit of a rough and ready script, but it gets the job done
#                                                   tk1748

$cgi = new CGI;
for $key ( $cgi->param() ) {
	$input{$key} = $cgi->param($key);
}

print "Content-Type: text/html\n\n";
$msgvalid = 500; #valid for 500 seconds

# Map command line to cgi bin
$ARGV[0] = $input{ip};
$ARGV[1] = $input{level};
$ARGV[2] = $input{name};
$ARGV[3] = $input{port};

$file = 'data.txt';
if (length $ARGV[0] == "" && length $ARGV[1] == "")
{	
    # Read it all in, and check for old ones, display a list
    # of servers
    open(INFO, $file);
	@lines = <INFO>;
	close(INFO);
        foreach $line (@lines)
        {
            @aline = split(/\|/,$line);
            if (scalar(@aline) != 6)
	    {
		print("bad line\n");
	    }
	    else
	    {
		if ($aline[0]+$msgvalid > time)
		{
		    push (@toadd,$line);
		}
	    }
	}
	open(INFO, ">$file") or die("Server busy, try again");
	foreach $line (@toadd) 
	{
	    print INFO $line;
	    # print the data out
	    print $line;
	}
    print INFO $toOutPut; #add our new one to the end
    close(INFO);
    print("OK");

}
else
{
    if (length $ARGV[0] != "" && length $ARGV[1] == "")
    {
	# request for the game with the specified ip to be removed
	# from the list
	open(INFO, $file);
	@lines = <INFO>;
	close(INFO);
        foreach $line (@lines)
        {
            @aline = split(/\|/,$line);
            if (scalar(@aline) != 6)
	    {
		print("bad line\n");
	    }
	    else
	    {
		if ($aline[0]+$msgvalid > time)
		{
		    # only if source ip and given ip are the same
		    if (($ARGV[0] eq $aline[2]) && $aline[1] eq $ENV{REMOTE_ADDR})
		    {
			# only if its not equal-
			# I told u the code was good
			print("REMOVED\n");
		    }
		    else
		    {
			push (@toadd,$line);
		    }
		}
	    }
	}
	open(INFO, ">$file") or die("Server busy, try again");
	foreach $line (@toadd) 
	{
	    print INFO $line;
	    # print the data out
	}
	print INFO $toOutPut; #add our new one to the end
	close(INFO);
	print("OK");
    }
    else
    {
	# We are adding the IP to the list
	if (length($ARGV[0]) + length($ARGV[1])+length($ARGV[2]) + length($ARGV[3]) < 200)
	{
	    $ip = $ARGV[0];
	    $ip =~ s/[^\w.]//g; # remove anything not alpha numeric or .
	    $level = $ARGV[1];
	    $level =~ s/[^\w]//g;
	    $name = $ARGV[2];
	    $name =~ s/[^\w]//g;
	    $port = $ARGV[3];
	    $port =~ s/[^\w]//g;
	    $toOutPut=(time."|$ENV{REMOTE_ADDR}|$ip|$level|$name|$port\n");
	    
	    #now read in file, go through array, taking out
	    # those older than 5 mins, and add ours to the end
	    open(INFO, $file);
	    @lines = <INFO>;
	    close(INFO);
	    foreach $line (@lines)
	    {
		@aline = split(/\|/,$line);
		if (scalar(@aline) != 6)
		{
		    print("bad line\n");
		}
		else
		{
		    if ($aline[0]+$msgvalid > time)
		    {
			push (@toadd,$line);
		    }
		}
	    }
	    open(INFO, ">$file") or die("Server busy, try again");
	    foreach $line (@toadd) 
	    {
		print INFO $line;
	    }
	    print INFO $toOutPut; #add our new one to the end
	    close(INFO);
	    print "OK: $toOutPut";
	}
    }
}
