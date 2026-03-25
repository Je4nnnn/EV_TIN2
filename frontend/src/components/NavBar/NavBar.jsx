import React, { useState} from 'react'
import { Link, NavLink } from 'react-router-dom' 
import './NavBar.css'
const NavBar = () => {
    const[menuOpen,setMenuOpen]= useState(false)
  return (
    <nav>
        <Link to="/" className="title">KartingRM</Link>
          <div className="menu" onClick={() => setMenuOpen(!menuOpen)}>
            <span></span>
            <span></span>
            <span></span>   
        </div>
        <ul className={menuOpen ? "open" : ""}>
            <li>
                <NavLink to="/reservation">Reservar</NavLink>
            </li>
            <li>
                <NavLink to="/tariffs">Tarifas</NavLink>
            </li>
            <li>
                <NavLink to="/Rack">Rack Semanal</NavLink>
            </li>
            <li>
                <NavLink to="/reports">Reportes</NavLink>
            </li>
            <li>
                <NavLink to="/contact">Contacto</NavLink>
            </li>
        </ul>
    </nav>
  )
}

export default NavBar
