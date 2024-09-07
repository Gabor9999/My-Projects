<a href="{{ route('logout') }}"
        id="trigger"
                onclick="event.preventDefault();
                    document.getElementById('logout-form').submit();">
</a>

<form id="logout-form" action="{{ route('logout') }}" method="POST" class="d-none">
    @csrf
</form>

<?php  
echo '<script type="text/JavaScript">  
        document.getElementById("trigger").click();
    </script>' 
; 
?>