import {useCallback, useEffect, useState} from 'react';

export default function useGetRequest(endpoint) { 
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const refetch = useCallback(() => {
        setLoading(true)
        setError(null)
        setData(null);
        fetch(endpoint).then(resp => {
            console.warn(resp)
            if (resp.ok === true) {
                resp.json().then(v => {
                    setData(v)
                    setLoading(false)
                })        
            } else {
                setError(`Something went wrong when communicating with our servers (STATUS=${resp.status})`)
                setLoading(false)
            }    
        }, error => {
            setError(error.toString());
            setLoading(false)
        })
    }, [endpoint])

    useEffect(() => {
        refetch()
    }, [endpoint, refetch])

    return { data, error, loading, refetch }
}