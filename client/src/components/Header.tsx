import { UseAppContext } from "@/ContextProvider";
import '../styles/header.scss';

export function MyHeader() {
  const { jwt, userInfo, logout } = UseAppContext();
  return (
    <header>
      <h1>Mis Tareas</h1>
      {jwt.trim() && (
        <div className="user-info">
          <span>{userInfo.nickname}</span>
          <button onClick={logout}>Log out</button>
        </div>
      )}
    </header>
  );
}
