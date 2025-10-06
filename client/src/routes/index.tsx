import { UseAppContext } from "@/ContextProvider";
import { createFileRoute, Navigate } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  component: App,
});

function App() {
  const { jwt } = UseAppContext();
  return <Navigate to={jwt.trim() ? "/mytasks" : "/login"} />;
}
