<script>
	let buttonProps = {
	class:[$$restProps.class]
	}
</script>
<button on:click
		on:mouseover
        on:focus
		on:mouseenter
		on:mouseleave
	{...buttonProps}>
		<slot/>
</button>

<style>
	.primary{
		color:#0db136;
        background-color: rgb(25, 25, 25);
        margin-bottom: 10px;
        width: 100%;
        padding-left: 25%;
        padding-right: 25%;
        padding-top: 5%;
        padding-bottom: 5%;
        font-size: 22px;
        font-weight: bold;
        font-family: Papyrus, fantasy;
        border-radius: 30px 30px;
        text-align: center;
	}
    .back {
        border: none;
        background: url('../../../public/back.png') no-repeat top left;
        height: 32px;
        width: 32px;
    }
    .secondary-1 {
        color:#0db136;
        background-color: rgb(25, 25, 25);
        border-radius: 30px 30px;
        font-family: Papyrus, fantasy;
        padding-left: 100px;
        padding-right: 100px;
        padding-top: 10px;
        padding-bottom: 10px;
    }
    .secondary-2 {
        color:#fffc2e;
        background-color: rgb(25, 25, 25);
        border-radius: 30px 30px;
        font-family: Papyrus, fantasy;
        padding-left: 100px;
        padding-right: 100px;
        padding-top: 10px;
        padding-bottom: 10px;
    }
    .secondary-3 {
        color:rgb(227, 11, 92);
        background-color: rgb(25, 25, 25);
        border-radius: 30px 30px;
        font-family: Papyrus, fantasy;
        padding-left: 100px;
        padding-right: 100px;
        padding-top: 10px;
        padding-bottom: 10px;
    }

    .active {
        border: 2px solid lightskyblue;
    }
</style>