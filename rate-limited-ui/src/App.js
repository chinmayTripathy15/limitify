import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [count, setCount] = useState(0);
  const [message, setMessage] = useState("");
  const [error, setError] = useState(false);
  const [cooldown, setCooldown] = useState(0);

  // ⏳ countdown timer
  useEffect(() => {
    if (cooldown > 0) {
      const timer = setTimeout(() => {
        setCooldown((prev) => prev - 1);
      }, 1000);

      return () => clearTimeout(timer);
    }
  }, [cooldown]);

  const callApi = async () => {
    // Block button during cooldown
    if (cooldown > 0) return;

    try {
      const res = await fetch("https://limitify-ks8x.onrender.com/api/test");

      // ❌ NOT OK RESPONSE (429, 500 etc.)
      if (!res.ok) {
        if (res.status === 429) {
          setMessage("🚫 Rate limit exceeded");
          setCooldown(60); // ⏳ start timer
          setError(true);
        } else {
          setMessage("⚠️ Backend error occurred");
          setError(true);
        }
        return;
      }

      // ✅ SUCCESS RESPONSE
      const data = await res.text();
      setCount((prev) => prev + 1);
      setMessage(data);
      setError(false);

    } catch (err) {
      // ❌ ONLY WHEN BACKEND IS REALLY DOWN
      setMessage("❌ Unable to connect to backend server");
      setError(true);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>🚦 API Rate Limiter Demo</h1>
        <p className="subtitle">
          Backend protected using Spring Boot Filter
        </p>

        <div className="count-box">
          API Calls Made: <span>{count}</span>
        </div>

        <button onClick={callApi} disabled={cooldown > 0}>
          {cooldown > 0 ? "Please wait..." : "Call API"}
        </button>

        {message && (
          <div className={error ? "error" : "success"}>
            {message}
            {cooldown > 0 && (
              <div style={{ marginTop: "8px" }}>
                ⏳ Try again in <b>{cooldown}</b> seconds
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
