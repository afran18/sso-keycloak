import { useEffect, useState } from "react";

function App() {

  const [user, setUser] = useState(null);

  const checkSession = async () => {
    try {
      const response = await fetch(
        "http://localhost:8081/profile",
        {
          credentials: "include"
        }
      );

      if (response.ok) {
        const data = await response.json();
        setUser(data);
      } else {
        window.location.href =
          "http://localhost:8081/auth/login";
      }

    } catch (error) {
      console.error(error);

      window.location.href =
        "http://localhost:8081/auth/login";
    }
  };

  useEffect(() => {
    checkSession();
  }, []);

  if (!user) {
    return <h2>Loading...</h2>;
  }

  return (
    <>
      <h1>Welcome {user.name}</h1>
      <p>{user.email}</p>

      <button
        onClick={() =>
          window.location.href =
            "http://localhost:8081/auth/logout"
        }
      >
        Logout
      </button>
    </>
  );
}

export default App;