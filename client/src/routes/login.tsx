import { UseAppContext } from "@/ContextProvider";
import { createFileRoute, Navigate } from "@tanstack/react-router";
import React, { type FormEvent } from "react";
import '../styles/login.scss';

export const Route = createFileRoute("/login")({
  component: RouteComponent,
});

function RouteComponent() {
  const [data, setData] = React.useState<LoginDto>({
    username: "",
    password: "",
  });
  const { login, message, jwt } = UseAppContext();
  const submit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    login(data);
  };
  if(jwt.trim()) return <Navigate to="/mytasks"/>
  return (
    <form onSubmit={submit} id="loginform">
      <input
        type="text"
        placeholder="Username"
        value={data.username}
        onChange={(e) => setData({ ...data, username: e.target.value })}
      />
      <input
        type="text"
        placeholder="Password"
        value={data.password}
        onChange={(e) => setData({ ...data, password: e.target.value })}
      />
      <button type="submit">Entrar</button>
      {message.trim()?<p>{message}</p>:null}
    </form>
  );
}
