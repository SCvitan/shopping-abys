"use client";

import { useAuth } from "@/lib/auth-context";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import useSWR from "swr";
import { fetchCart } from "@/lib/api";
import { SiteHeader } from "@/components/site-header";
import { CartItemRow } from "@/components/cart-item-row";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { ShoppingCart, ArrowLeft } from "lucide-react";
import Link from "next/link";

export default function CartPage() {
  const { user, loading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!loading && !user) {
      router.push("/login");
    }
  }, [loading, user, router]);

  const { data: cart, isLoading: cartLoading } = useSWR(
    user ? "cart" : null,
    fetchCart
  );

  if (loading || !user) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <div className="h-6 w-6 animate-spin rounded-full border-2 border-primary border-t-transparent" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <SiteHeader />
      <main className="mx-auto max-w-2xl px-4 py-8">
        <Button variant="ghost" asChild className="mb-6 gap-1.5 text-muted-foreground">
          <Link href="/">
            <ArrowLeft className="h-4 w-4" />
            Back to products
          </Link>
        </Button>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2 text-foreground">
              <ShoppingCart className="h-5 w-5" />
              Your Cart
            </CardTitle>
          </CardHeader>
          <CardContent>
            {cartLoading ? (
              <div className="flex flex-col gap-4">
                {Array.from({ length: 3 }).map((_, i) => (
                  <div key={i} className="flex items-center justify-between py-4">
                    <div className="flex flex-col gap-2">
                      <Skeleton className="h-4 w-40" />
                      <Skeleton className="h-3 w-24" />
                    </div>
                    <Skeleton className="h-4 w-16" />
                  </div>
                ))}
              </div>
            ) : !cart?.items?.length ? (
              <div className="py-12 text-center">
                <ShoppingCart className="mx-auto mb-3 h-10 w-10 text-muted-foreground opacity-40" />
                <p className="text-muted-foreground">Your cart is empty.</p>
                <Button variant="outline" asChild className="mt-4">
                  <Link href="/">Browse products</Link>
                </Button>
              </div>
            ) : (
              <>
                <div className="flex flex-col">
                  {cart.items.map((item) => (
                    <CartItemRow key={item.productId} item={item} />
                  ))}
                </div>
                <Separator className="my-4" />
                <div className="flex items-center justify-between">
                  <span className="text-lg font-semibold text-foreground">Total</span>
                  <span className="text-lg font-semibold text-foreground">
                    ${cart.totalPrice.toFixed(2)}
                  </span>
                </div>
              </>
            )}
          </CardContent>
        </Card>
      </main>
    </div>
  );
}
