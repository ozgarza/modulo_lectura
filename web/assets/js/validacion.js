$(function() {
  
  //pagina de login
  $('#usuario').validate( {
    expression: "if(VAL) return true; else return false;",
    message: "No puede estar vacio"
  });

  $('#pwd').validate( {
    expression: "if(VAL) return true; else return false;",
    message: "No puede estar vacio"
  });
  //termina pagina de login

  //empieza pagina de registrar
  $('#matricula').validate( {
    expression: "if (!isNaN(VAL) && VAL) return true; else return false;",
    message: "Formato inválido"
  });

  $('#contrasenia').validate( {
    expression: "if (VAL) return true; else return false;",
    message: "No puede estar vacio"
  });

  $('#contraseniaC').validate( {
    expression: "if(VAL == $('#contrasenia').val() && VAL) return true; else return false;",
    message: "Las contraseñas deben ser iguales"
  });

  $('#nombre').validate( {
    expression: "if (VAL) return true; else return false;",
    message: "No puede estar vacio"
  });

  $('#apellidoP').validate( {
    expression: "if (VAL) return true; else return false;",
    message: "No puede estar vacio"
  });

  $('#apellidoM').validate( {
    expression: "if (VAL) return true; else return false;",
    message: "No puede estar vacio"
  });

  $('#departamento').validate( {
    expression: "if (VAL != '0') return true; else return false;",
    message: "Seleccione un departamento"
  });

  jQuery("#email").validate({
    expression: "if (VAL.match(/^[^\\W][a-zA-Z0-9\\_\\-\\.]+([a-zA-Z0-9\\_\\-\\.]+)*\\@[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*\\.[a-zA-Z]{2,4}$/)) return true; else return false;",
    message: "Formato de email no válido"
  });

});